import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import './DetailView.css'
import './WerteView.css'

interface Wertebereich {
  min: number
  max: number
  average: number
}

function fromXml(xml: string | null): Wertebereich {
  if (!xml) return { min: -10, max: 10, average: 0 }
  const doc = new DOMParser().parseFromString(xml, 'application/xml')
  const el = doc.getElementsByTagName('wertebereich')[0]
  if (!el) return { min: -10, max: 10, average: 0 }
  return {
    min: parseInt(el.getAttribute('min') ?? '-10', 10),
    max: parseInt(el.getAttribute('max') ?? '10', 10),
    average: parseInt(el.getAttribute('average') ?? '0', 10),
  }
}

function toXml(base: string, w: Wertebereich): string {
  const doc = new DOMParser().parseFromString(base, 'application/xml')
  let el: Element | null = doc.getElementsByTagName('wertebereich')[0] ?? null
  if (!el) {
    el = doc.createElement('wertebereich')
    doc.documentElement.insertBefore(el, doc.documentElement.firstChild)
  }
  el.setAttribute('min', String(w.min))
  el.setAttribute('max', String(w.max))
  el.setAttribute('average', String(w.average))
  return new XMLSerializer().serializeToString(doc)
}

export function WerteView() {
  const navigate = useNavigate()
  const { xmlContent, setXmlContent, fileHandle, currentRuleset } = useRuleset()
  const [werte, setWerte] = useState<Wertebereich>(() => fromXml(xmlContent))
  const [saved, setSaved] = useState(false)
  const [saveError, setSaveError] = useState<string | null>(null)

  const isValid = werte.min <= werte.average && werte.average <= werte.max

  async function save() {
    if (!xmlContent || !isValid) return
    const xml = toXml(xmlContent, werte)
    setXmlContent(xml)
    setSaveError(null)
    if (fileHandle) {
      try {
        const w = await fileHandle.createWritable()
        await w.write(xml)
        await w.close()
      } catch (err) {
        const msg = err instanceof Error ? err.message : String(err)
        setSaveError(`Datei konnte nicht geschrieben werden: ${msg}`)
        return
      }
    } else {
      const blob = new Blob([xml], { type: 'application/xml' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = currentRuleset ?? 'regelwerk.xml'
      a.click()
      URL.revokeObjectURL(url)
    }
    setSaved(true)
    setTimeout(() => setSaved(false), 1800)
  }

  function update(field: keyof Wertebereich, raw: string) {
    const val = parseInt(raw, 10)
    if (!isNaN(val)) setWerte(prev => ({ ...prev, [field]: val }))
    setSaved(false)
  }

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/edit-ruleset')}>← Zurück</button>
      <h1>Wertebereich</h1>

      <div className="werte-form">
        <div className="werte-row">
          <label className="werte-label" htmlFor="werte-min">Unterster Wert</label>
          <input
            id="werte-min"
            type="number"
            className="werte-input"
            value={werte.min}
            onChange={e => update('min', e.target.value)}
          />
        </div>

        <div className="werte-row">
          <label className="werte-label" htmlFor="werte-avg">Durchschnittswert</label>
          <input
            id="werte-avg"
            type="number"
            className="werte-input"
            value={werte.average}
            onChange={e => update('average', e.target.value)}
          />
        </div>

        <div className="werte-row">
          <label className="werte-label" htmlFor="werte-max">Oberster Wert</label>
          <input
            id="werte-max"
            type="number"
            className="werte-input"
            value={werte.max}
            onChange={e => update('max', e.target.value)}
          />
        </div>

        {!isValid && (
          <p className="werte-error">
            Ungültige Werte: unterster ≤ Durchschnitt ≤ oberster Wert erforderlich.
          </p>
        )}
        {saveError && <p className="werte-error">{saveError}</p>}

        <button
          className="werte-save-btn"
          onClick={save}
          disabled={!isValid || !xmlContent}
        >
          {saved ? '✓ Gespeichert' : 'Speichern'}
        </button>
      </div>
    </div>
  )
}
