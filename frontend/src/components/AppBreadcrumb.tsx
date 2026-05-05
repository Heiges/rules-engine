import { Breadcrumb } from './Breadcrumb'
import { useRuleset } from '../context/RulesetContext'
import type { LabelMap, DynamicResolver } from '../hooks/useBreadcrumbs'

export function AppBreadcrumb() {
  const { currentRuleset, rulesetData } = useRuleset()

  const labels: LabelMap = {
    '/': 'Rules Engine',
    '/home': 'Spielleiter',
    '/player': 'Spieler',
    '/edit-ruleset': currentRuleset ?? 'Regelwerk',
    '/create-world': 'Spielwelt',
    '/tile/werte': 'Werte',
    '/tile/attributes': 'Attribute',
    '/world/attributes': 'Attribute',
    '/tile/skills': 'Skills',
    '/tile/skills/domains': 'Domänen',
    '/tile/cheats': 'Cheats',
    '/character-editor': 'Charactereditor',
  }

  const resolve: DynamicResolver = (segment, fullPath) => {
    const index = parseInt(segment, 10)
    if (isNaN(index) || !rulesetData) return null

    if (fullPath.startsWith('/tile/attributes/')) return rulesetData.attributes[index]?.name ?? null
    if (fullPath.startsWith('/world/attributes/')) return rulesetData.attributes[index]?.name ?? null
    if (fullPath.startsWith('/tile/skills/domains/')) return rulesetData.skillDomains[index]?.name ?? null
    if (fullPath.startsWith('/tile/skills/')) return rulesetData.skills[index]?.name ?? null
    if (fullPath.startsWith('/tile/cheats/')) return rulesetData.cheats[index]?.name ?? null
    return null
  }

  return <Breadcrumb labels={labels} resolve={resolve} />
}
